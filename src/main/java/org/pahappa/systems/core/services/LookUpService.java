package org.pahappa.systems.core.services;

import com.googlecode.genericdao.search.Search;
import java.util.List;
import org.pahappa.systems.models.LookUpField;
import org.pahappa.systems.models.LookUpValue;

import org.sers.webutils.model.exception.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link LookUpField}
 * 
 * @author mmc
 *
 */
public interface LookUpService extends GenericService<LookUpField>{
	/**
	 * Adds a lookUpField to the database.
	 * 
	 * @param lookUpField
     * @return 
	 * @throws ValidationFailedException if the following attributes are blank:
	 *                                   name, phoneNumber
	 */
	LookUpField save(LookUpField lookUpField) throws ValidationFailedException;

	/**
	 * Gets a list of lookUpFields that match the specified search criteria
	 * 
	 * @param searchTerm
	 * @param church
	 * @param sortField
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<LookUpField> getLookUpFields(Search search, int offset, int limit);

	/**
	 * Counts a list of lookUpFields that match the specified search criteria
	 * 
	 * @param searchTerm
	 * @param church
	 * @param sortField
	 * @return
	 */
	int countLookUpFields(Search search);

	/**
	 * Gets a lookUpField that matches the specified identifier
	 * 
	 * @param lookUpFieldId
	 * @return
	 */
	LookUpField getLookUpFieldById(String lookUpFieldId);

	/**
	 * Deactivates a lookUpField along with all he data associated to it. This lookUpField
	 * will never be accessible on the UI
	 * 
	 * @param lookUpField
	 * @throws ValidationFailedException
	 */
	void delete(LookUpField lookUpField) throws ValidationFailedException;

	/**
         * 
         * @param name
         * @return 
         */
        LookUpField getLookUpFieldByName(String name);     
        
        LookUpValue getLookUpValueById(String id);     
        
       LookUpValue findValueInLookUp(String lookUpFieldName, String lookUpValueName);
        
}
