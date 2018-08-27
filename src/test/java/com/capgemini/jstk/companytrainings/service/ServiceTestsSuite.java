package com.capgemini.jstk.companytrainings.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EmployeeServiceTest.class, TrainingServiceTest.class, ExternalCouchServiceTest.class})
public class ServiceTestsSuite {
}
