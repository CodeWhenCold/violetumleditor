package com.horstmann.violet.product.diagram.classes;



import java.io.Serializable;

import com.horstmann.violet.product.diagram.property.text.LineText;

	public class ClassStatistics  {

		private int size;
		private LineText nameOfClass;

		
		public ClassStatistics(LineText name, int numOfMeth){
		
			this.nameOfClass=name;
			this.size=numOfMeth;
		}

		public ClassStatistics() {
			// TODO Auto-generated constructor stub
		}


		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public LineText getNameOfClass() {
			return nameOfClass;
		}

		public void setNameOfClass(LineText nameOfClass) {
			this.nameOfClass = nameOfClass;
		}

		@Override
		public String toString() {
			return ("Name of Class: " + this.getNameOfClass() + " Size of Class: " + this.getSize() );
		}
		public String toStringCouple() {
			return ("Name of Class: " + this.getNameOfClass() + " CBO: " + this.getSize() );
		}


		
}
