/*
 Copyright (c) 2008 thePlatform, Inc.

This file is part of Cuanto, a test results repository and analysis program.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package cuanto

import grails.util.GrailsUtil

class InitializationService {

	boolean transactional = true



	void initTestResults() {
		if (TestResult.list().size() <= 0) {
			// set up the base TestResult types
			def resultList = []
			TestResult run = new TestResult(name: "Pass", includeInCalculations: true, isFailure: false)
			resultList += run

			TestResult fail = new TestResult(name: "Fail", includeInCalculations: true, isFailure: true)
			resultList += fail

			TestResult error = new TestResult(name: "Error", includeInCalculations: true, isFailure: true)
			resultList += error

			TestResult ignore = new TestResult(name: "Ignore", includeInCalculations: false, isFailure: false)
			resultList += ignore

			TestResult skip = new TestResult(name: "Skip", includeInCalculations: false, isFailure: false)
			resultList += skip

			TestResult unexecuted = new TestResult(name: "Unexecuted", includeInCalculations: false, isFailure: false)
			resultList += unexecuted

			resultList.each {result ->
				if (!result.save()) {
					result.errors.allErrors.each {
						log.warning it.toString()
					}
				}
			}
		}
	}


	void initAnalysisStates() {
		if (AnalysisState.list().size() <= 0) {
			def analysisList = []

			analysisList << new AnalysisState(name: "Unanalyzed", isAnalyzed: false, isDefault: true, isBug: false)
			analysisList << new AnalysisState(name: "Bug", isAnalyzed: true, isDefault: false,  isBug: true)
			analysisList << new AnalysisState(name: "Environment", isAnalyzed: true, isDefault: false,  isBug: false)
			analysisList << new AnalysisState(name: "Harness", isAnalyzed: true, isDefault: false, isBug: false)
			analysisList << new AnalysisState(name: "No Repro", isAnalyzed: true, isDefault: false, isBug: false)
			analysisList << new AnalysisState(name: "Other", isAnalyzed: true, isDefault: false, isBug: false)
			analysisList << new AnalysisState(name: "Test Bug", isAnalyzed: true, isDefault: false, isBug: false)
			analysisList << new AnalysisState(name: "Investigate", isAnalyzed: false, isDefault: false, isBug: false)

			analysisList.each {analysis ->
				if (!analysis.save()) {
					analysis.errors.allErrors.each {
						log.warning it.toString()
					}
				}
			}
		}
	}


	void initTestTypes() {
		if (TestType.list().size() <= 0) {
			def typeList = []

			typeList += new TestType(name: "JUnit")
			typeList += new TestType(name: "NUnit")
			typeList += new TestType(name: "TestNG")
			typeList += new TestType(name: "Selenium")
			typeList += new TestType(name: "Canoo")
			typeList += new TestType(name: "Manual")

			typeList.each {tp ->
				if (!tp.save()) {
					tp.errors.allErrors.each {
						log.warning it.toString()
					}
				}
			}
		}
	}


	void initProjects() {
		if (GrailsUtil.environment == "development") {
			def rnd = new Random()
			5.times {
				if (!Project.findByName("CuantoProd$it")) {
					def grp = new ProjectGroup(name: "Sample$it").save()
					rnd.nextInt(10).times { prjIndex ->
						new Project(name: "CuantoProd$prjIndex", projectKey: "CUANTO$prjIndex", projectGroup: grp,
						bugUrlPattern: "http://tpjira/browse/{BUG}", testType: TestType.findByName("JUnit")).save()
					}
				}

				// create ungrouped projects
				new Project(name: "Ungrouped-$it", projectKey: "Ungrouped-$it",
				bugUrlPattern: "http://tpjira/browse/{BUG}", testType: TestType.findByName("JUnit")).save()
			}
		}
	}


	void initializeAll() {
		initTestResults()
		initAnalysisStates()
		initTestTypes()
		initProjects()
	}
}
