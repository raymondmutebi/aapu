package org.pahappa.systems.core.services;

import com.googlecode.genericdao.search.Search;
import java.util.List;

import org.pahappa.systems.models.Member;
import org.sers.webutils.model.exception.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link Member}
 *
 * @author Ray Gdhrt
 *
 */
public interface MemberService extends GenericService<Member>{

    /**
     * Adds a manufacturer to the database.
     *
     * @param manufacturer
     * @return
     * @throws ValidationFailedException if the following attributes are blank:
     * name, phoneNumber
     */
    Member save(Member manufacturer) throws ValidationFailedException;

    /**
     * Adds a manufacturer to the database outside the spring security context.
     *
     * @param manufacturer
     * @return
     * @throws ValidationFailedException
     */
    Member saveOutsideContext(Member manufacturer) throws ValidationFailedException;

    /**
     * Gets a list of manufacturers that match the specified search criteria
     *
     * @param searchTerm
     * @param sortField
     * @param offset
     * @param limit
     * @return
     */
    List<Member> getManufacturers(Search search, int offset, int limit);

    /**
     * Counts a list of manufacturers that match the specified search criteria
     *
     * @param searchTerm
     * @param sortField
     * @return
     */
    int countManufacturers(Search search);

    /**
     * Gets a manufacturer that matches the specified identifier
     *
     * @param manufacturerId
     * @return
     */
    Member getManufacturerById(String manufacturerId);

    /**
     * Deactivates a manufacturer along with all he data associated to it. This
     * manufacturer will never be accessible on the UI
     *
     * @param manufacturer
     * @throws ValidationFailedException
     */
    void delete(Member manufacturer) throws ValidationFailedException;

    /**
     * Gets a manufacturer that matches the specified identifier
     *
     * @param phoneNumber
     * @return
     */
    Member getManufacturerByPhoneNumber(String phoneNumber);
}
