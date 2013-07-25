package org.alexbezverkhniy.csvloader.dbloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import au.com.bytecode.opencsv.CSVReader;

public class BaseDatabaseLoader implements DatabaseLoader {

	private DataSource dataSource;
	
	private String url;
	private String driverClassName;
	private String username;
	private String userpassword;
	
	private char seprator;
	private Connection connection;
	private CSVReader csvReader;
	private String tableName;
	private String columnNames;
	private String dateFormat;
	private Integer batchSize = 1000;

	/**
	 * Data load logic
	 * 
	 * @throws ParseException
	 */
	@Override
	public void	loadData(File inputDataFile, boolean truncate) throws SQLException, ClassNotFoundException, IOException {

		String values = "";
		// Title and count of columns
		for (String s : getCSVReader(inputDataFile).readNext()) {
			values += "?,";
		}
		values = values.substring(0, values.length() - 1);

		String query = "INSERT INTO " + tableName + "(" + columnNames + ") VALUES (" + values + ")";

		Connection con = getConnection();
		con.setAutoCommit(false);
		
		//Truncate date
		if(truncate) {
			con.prepareStatement("DELETE FROM " + tableName).executeUpdate();
		}
		
		PreparedStatement ps = con.prepareStatement(query);
		int count = 0;		
		String[] nextLine = null;
		try {
			while ((nextLine = csvReader.readNext()) != null) {

				if (nextLine != null) {
					int index = 1;
					for (String string : nextLine) {
						Date dateVal = null;
						Float floatVal = null;
						Integer intVal = null;

						if ((dateVal = parseDate(string)) != null) {
							ps.setDate(index++, new java.sql.Date(dateVal.getTime()));							
						} else if ((floatVal = parseFloat(string)) != null) {
							ps.setFloat(index++, floatVal);							
						} else if ((intVal = parseInteger(string)) != null) {
							ps.setInt(index++, intVal);
						} else {
							ps.setString(index++, string);
						}
					}
					ps.addBatch();
				}
				if (++count % batchSize == 0) {
					ps.executeBatch();
				}
			}
			ps.executeBatch(); // insert remaining records
			con.commit();
		} catch (Exception e) {
			con.rollback();
			e.printStackTrace();
			throw new SQLException("Error occured while loading data from file to database." + e.getMessage());
		} finally {
			if (null != ps)
				ps.close();
			if (null != con)
				con.close();

			csvReader.close();
		}

	}

	protected Date parseDate(String str) {
		Date val = null;		
		if (dateFormat == null || dateFormat.isEmpty()) {
			dateFormat = "yyyy.MM.dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		try {
			val = sdf.parse(str);
		} catch (ParseException e) {
			// TODO
		}

		return val;
	}

	protected Float parseFloat(String str) {
		Float val = null;
		boolean isHasDigits = str.matches("[+-]?\\d*(\\.\\d+)?(\\,\\d+)?"); 
		// Is Float value
		if (isHasDigits && (str.indexOf(",") >= 0 || str.indexOf(".") >= 0)) {
			try {
				val = Float.parseFloat(str.replace(",", ".").replace(" ", ""));
			} catch (NumberFormatException e) {
				// TODO
			}
		}

		return val;
	}

	protected Integer parseInteger(String str) {
		Integer val = null;		
		// Is Float value
		if (str.replace(" ", "").matches("[+-]?\\d*(\\.\\d+)?")) {
			try {
				val = Integer.parseInt(str.replace(" ", ""));
			} catch (NumberFormatException e) {
				// TODO
			}
		}

		return val;
	}

	/**
	 * Get DB connection
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	protected Connection getConnection() throws SQLException, ClassNotFoundException {
		/*
		if (connection == null || connection.isClosed()) {
			Class.forName(driverClassName);
			connection = DriverManager.getConnection(url, username, userpassword);
		}
		*/
		if ((connection == null || connection.isClosed()) && dataSource != null) {
			connection = dataSource.getConnection();
		}
		return connection;
	}

	/**
	 * Get DB connection
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	protected CSVReader getCSVReader(File file) throws FileNotFoundException {
		csvReader = new CSVReader(new FileReader(file), seprator);

		return csvReader;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserpassword() {
		return userpassword;
	}

	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}

	public char getSeprator() {
		return seprator;
	}

	public void setSeprator(char seprator) {
		this.seprator = seprator;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String columnNames) {
		this.columnNames = columnNames;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Integer getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(Integer batchSize) {
		this.batchSize = batchSize;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
