package com.horstmann.violet.framework.file.chooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.LocalFile;
import com.horstmann.violet.framework.file.naming.ExtensionFilter;
import com.horstmann.violet.framework.file.naming.FileNamingService;
import com.horstmann.violet.framework.file.persistence.IFileReader;
import com.horstmann.violet.framework.file.persistence.IFileWriter;
import com.horstmann.violet.framework.file.persistence.JFileReader;
import com.horstmann.violet.framework.file.persistence.JFileWriter;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.framework.userpreferences.UserPreferencesService;
import com.horstmann.violet.framework.file.statistics;

/**
 * This class implements a FileService with a JFileChooser
 */
@ManagedBean(registeredManually=true)
public class JFileChooserService implements IFileChooserService{
	
    public JFileChooserService()
    {
        ResourceBundleInjector.getInjector().inject(this);
        BeanInjector.getInjector().inject(this);
        this.currentDirectory = getLastOpenedDir();
    }

    /**
     * @return the last opened file directory or the current directory if no one is found
     */
    private File getLastOpenedDir()
    {
        List<IFile> recentFiles = this.userPreferencesService.getRecentFiles();
        for (IFile aFile : recentFiles) {
        	try {
				LocalFile localFile = new LocalFile(aFile);
				File lastDir = new File(localFile.getDirectory());
				return lastDir;
			} catch (IOException e) {
			}
        }
        File currentDir = new File(System.getProperty("user.home"));
        return currentDir;
    }

    @Override
    public boolean isWebStart()
    {
        return false;
    }

    @Override
    public IFileReader getFileReader(IFile file) throws FileNotFoundException
    {
        try
        {
            LocalFile localFile = new LocalFile(file);
            File physicalFile = localFile.toFile();
            if (physicalFile.exists() && physicalFile.isFile())
            {
                IFileReader foh = new JFileReader(physicalFile);
                return foh;
            }
            else
            {
                throw new FileNotFoundException("File " + file.getFilename() + " not reachable from " + file.getDirectory());
            }
        }
        catch (IOException e1)
        {
            throw new FileNotFoundException(e1.getLocalizedMessage());
        }
    }
	static Login2 loginObj;
	static boolean generateStats = false;
	
    @Override
    public IFileReader chooseAndGetFileReader(ExtensionFilter... filters) throws FileNotFoundException, UnsupportedEncodingException
    {
    	JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(this.currentDirectory);
    	//ExtensionFilter[] filters = fileNamingService.getFileFilters();
        for (int i = 0; i < filters.length; i++)
        {
            fileChooser.addChoosableFileFilter(filters[i]);
            fileChooser.setFileFilter(filters[i]); // Set current filter to the last one  
        }
        int response = fileChooser.showOpenDialog(null);
        File selectedFile = null;
        if (response == JFileChooser.APPROVE_OPTION)
        {
        	//place user at last directory they used
            this.currentDirectory = fileChooser.getCurrentDirectory();
        	
            //gets the file the user selected
            selectedFile = fileChooser.getSelectedFile();
        	
            //if the user selected the test txt file that was made to represent stats
        	//create instance of login
        	if (selectedFile.getName().contains(".seq.violet.txt")){
        		
        		// if a login instance hasn't been created once the stat file is selected create a login instance
        		if (loginObj == null){
        			loginObj = new Login2();
        			
        			//get the whole set up created in login class for the window prompt for credentials
        			JOptionPane optionPane = loginObj.getOptionPane();
        			this.dialogFactory.showDialog(optionPane, "Login", true);
        			
        			//checking if loginCheck boolean returned is true to confirm if stat file can be accessed and generated in violet uml
        			if (loginObj.loginCheck() == true)
        				generateStats = true;
            	}
        		if (generateStats == true){
        			statistics statsFile = null;
        			try{
        				//creating a new instance of a stats file to create a violet uml window representation of what file contains
        				statsFile = new statistics(selectedFile);
        			}
    				catch (IOException e){
    					e.printStackTrace(System.out);
        			}
        		}
        		return null;
            }
        }
        if (response == JFileChooser.CANCEL_OPTION)
        {
        	this.currentDirectory = fileChooser.getCurrentDirectory();
        }
        if (selectedFile == null)
        {
            return null;
        }
        IFileReader foh = new JFileReader(selectedFile);
        return foh;
    }

    @Override
    public IFileWriter getFileWriter(IFile file) throws FileNotFoundException
    {
        try
        {
            LocalFile localFile = new LocalFile(file);
            IFileWriter fsh = new JFileWriter(localFile.toFile());
            return fsh;
        }
        catch (IOException e)
        {
            throw new FileNotFoundException(e.getLocalizedMessage());
        }
    }

    @Override
    public IFileWriter chooseAndGetFileWriter(ExtensionFilter... filters) throws FileNotFoundException, UnsupportedEncodingException
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(this.currentDirectory);
        fileChooser.setAcceptAllFileFilterUsed(false);
        for (int i = 0; i < filters.length; i++)
        {
            ExtensionFilter aFilter = filters[i];
            fileChooser.addChoosableFileFilter(aFilter);
            fileChooser.setFileFilter(aFilter);
        }
        int response = fileChooser.showSaveDialog(null);
        File selectedFile = null;
        if (response == JFileChooser.APPROVE_OPTION)
        {
            this.currentDirectory = fileChooser.getCurrentDirectory();
        	selectedFile = fileChooser.getSelectedFile();
            ExtensionFilter selectedFilter = (ExtensionFilter) fileChooser.getFileFilter();
            String fullPath = selectedFile.getAbsolutePath();
            String extension = selectedFilter.getExtension();
            if (!fullPath.toLowerCase().endsWith(extension)) {
                fullPath = fullPath + extension;
                selectedFile = new File(fullPath);
            }
            if (selectedFile.exists())
            {
                JOptionPane optionPane = new JOptionPane();
                optionPane.setMessage(this.overwriteDialogBoxMessage);
                optionPane.setOptionType(JOptionPane.YES_NO_OPTION);
                optionPane.setIcon(this.overwriteDialogBoxIcon);
                this.dialogFactory.showDialog(optionPane, this.overwriteDialogBoxTitle, true);

                int result = JOptionPane.NO_OPTION;
                if (!JOptionPane.UNINITIALIZED_VALUE.equals(optionPane.getValue()))
                {
                    result = ((Integer) optionPane.getValue()).intValue();
                }

                if (result == JOptionPane.NO_OPTION)
                {
                    selectedFile = null;
                }
            }
        }
        if (response == JFileChooser.CANCEL_OPTION)
        {
        	this.currentDirectory = fileChooser.getCurrentDirectory();
        }
        if (selectedFile == null)
        {
            return null;
        }
        IFileWriter fsh = new JFileWriter(selectedFile);
        return fsh;
    }    
    

    @InjectedBean
    private UserPreferencesService userPreferencesService;
    
    @InjectedBean
    private FileNamingService fileNamingService;
    
    @InjectedBean
    private DialogFactory dialogFactory;

    @ResourceBundleBean(key="dialog.overwrite.ok")
    private String overwriteDialogBoxMessage;

    @ResourceBundleBean(key="dialog.overwrite.title")
    private String overwriteDialogBoxTitle;

    @ResourceBundleBean(key="dialog.overwrite.icon")
    private ImageIcon overwriteDialogBoxIcon;

    /** Keeps current directory to always place the user to the last directory he worked with  */
    private File currentDirectory;

}
