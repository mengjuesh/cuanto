/*

Copyright (c) 2010 Todd Wells

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

package cuanto.queryprocessor

import cuanto.QueryFilter
import cuanto.TestOutcome


public class AnalysisStateQueryModule implements QueryModule {

	/**
	 * If queryFilter.analysisState is not null, only TestOutcomes with this AnalysisState will be returned.
	 */
	public Map getQueryParts(QueryFilter queryFilter) {
		def map = [:]
		if (queryFilter.analysisState) {
			map = [where: " t.analysisState = ? ", params: [queryFilter.analysisState]]
		}
		return map
	}


	public List<Class> getObjectTypes() {
		[TestOutcome.class]
	}

}