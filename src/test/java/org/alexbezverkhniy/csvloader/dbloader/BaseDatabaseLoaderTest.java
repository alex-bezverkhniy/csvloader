package org.alexbezverkhniy.csvloader.dbloader;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaseDatabaseLoaderTest {

	private BaseDatabaseLoader databaseLoader;
	private File csvFile;

	@Before
	public void setUp() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:test-application-context.xml");
		databaseLoader = (BaseDatabaseLoader) context.getBean("cheques");
		csvFile = new File("sample-data.csv");
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testLoadData() {
		try {
			databaseLoader.loadData(csvFile, true);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs = databaseLoader.getDataSource().getConnection().prepareStatement("SELECT count(*) FROM public.cheques").executeQuery();
			assertNotNull(rs);
			assertTrue(rs.next());
			assertEquals(rs.getInt(1), 20);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
