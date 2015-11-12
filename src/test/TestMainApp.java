package test;

import static org.junit.Assert.*;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.*;
import org.junit.contrib.java.lang.system.SystemOutRule;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import main.MainApp;

public class TestMainApp extends TestCase  {

@Rule
public TextFromStandardInputStream systemInMock = emptyStandardInputStream();

@Rule
public SystemOutRule systemOutRule = new SystemOutRule().enableLog();

@Test
public void testListServerAction() {
	boolean success = true;

	String[] args = {"test/data/server_1.xml"};

	try {
	  systemInMock.provideLines("countServers");
	  MainApp.main(args);

	  System.out.println("result string" +systemOutRule.getLog());
	  assertEquals("3", systemOutRule.getLog());
	} catch(Exception e) {
	  System.out.println(e.getMessage());
	  success=false;
	}

	//assertTrue(success);
}
}