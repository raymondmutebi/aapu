/**
 * 
 */
package org.pahappa.systems.core.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.security.User;
import org.sers.webutils.model.utils.SearchField;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

/**
 * Contains the general search utility/helper functions used to query models
 * from the DB.
 * 
 * @author Mzee Sr.
 *
 */
public class GeneralSearchUtils {

	private static final int MINIMUM_CHARACTERS_FOR_SEARCH_TERM = 1;

	public static boolean searchTermSatisfiesQueryCriteria(String query) {
		if (StringUtils.isBlank(query))
			return false;
		return query.length() >= MINIMUM_CHARACTERS_FOR_SEARCH_TERM;
	}

	public static boolean generateSearchTerms(List<SearchField> searchFields, String query, List<Filter> filters) {
		if (searchFields != null && !searchFields.isEmpty()) {
			for (String token : query.replaceAll("  ", " ").split(" ")) {
				String searchTerm = "%" + StringEscapeUtils.escapeSql(token) + "%";
				Filter[] orFilters = new Filter[searchFields.size()];
				int counter = 0;
				for (SearchField searchField : searchFields) {
					orFilters[counter] = Filter.like(searchField.getPath(), searchTerm);
					counter++;
				}
				filters.add(Filter.or(orFilters));
			}
			return true;
		}
		return false;
	}

	public static boolean generateSearchTerms(String commaSeparatedsearchFields, String query, List<Filter> filters) {
		if (StringUtils.isBlank(commaSeparatedsearchFields))
			return false;

		List<String> searchFields = Arrays.asList(commaSeparatedsearchFields.split(","));
		if (searchFields != null && !searchFields.isEmpty()) {
			for (String token : query.replaceAll("  ", " ").split(" ")) {
				String searchTerm = "%" + StringEscapeUtils.escapeSql(token) + "%";
				Filter[] orFilters = new Filter[searchFields.size()];
				int counter = 0;
				for (String searchField : searchFields) {
					orFilters[counter] = Filter.like(searchField, searchTerm);
					counter++;
				}
				filters.add(Filter.or(orFilters));
			}
			return true;
		}
		return false;
	}

	public static Search composeUsersSearch(List<SearchField> searchFields, String query) {
		Search search = new Search();
		search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
		search.addSortAsc("username");
		search.addFilterNotIn("username", User.DEFAULT_ADMIN);

		if (StringUtils.isNotBlank(query) && GeneralSearchUtils.searchTermSatisfiesQueryCriteria(query)) {
			ArrayList<Filter> filters = new ArrayList<Filter>();
			GeneralSearchUtils.generateSearchTerms(searchFields, query, filters);
			search.addFilterAnd(filters.toArray(new Filter[filters.size()]));
		}
		return search;
	}
}
