package org.alexbezverkhniy.csvloader.dbloader;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Class for loading to DB  data of - "individual payments cashiers"
 * Индивидуальные показатели кассиров
 *  
 * @author Alexander Bezverkhniy
 *
 */
public class IndivPaymentsCashiersLoader extends BaseDatabaseLoader{

	/**
	 * Reading .CSV file and load the data to DB
	 *
	@Override
	public void loadData(File inputDataFile) throws SQLException, ClassNotFoundException, IOException {
		
		//Title
		for (String s : getCSVReader(inputDataFile).readNext()) {
			System.out.println(s);
		}	
	}
	*/

}
