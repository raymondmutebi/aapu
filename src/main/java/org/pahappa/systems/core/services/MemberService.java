package org.pahappa.systems.core.services;

import com.googlecode.genericdao.search.Search;
import java.util.List;

import org.pahappa.systems.models.Member;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.security.User;

/**
 * Responsible for CRUD operations on {@link Member}
 *
 * @author Ray Gdhrt
 *
 */
public interface MemberService extends GenericService<Member> {

    /**
     * Adds a member to the database.
     *
     * @param member
     * @return
     * @throws ValidationFailedException if the following attributes are blank:
     * name, phoneNumber
     */
    Member save(Member member) throws ValidationFailedException;

    /**
     * Adds a member to the database outside the spring security context.
     *
     * @param member
     * @return
     * @throws ValidationFailedException
     */
    Member saveOutsideContext(Member member) throws ValidationFailedException;

    /**
     *
     * @param member
     * @return
     * @throws ValidationFailedException
     * @throws OperationFailedException
     */
    Member saveUploadedMember(Member member) throws ValidationFailedException,Exception, OperationFailedException;

    /**
     * Gets a list of members that match the specified search criteria
     *
     * @param searchTerm
     * @param sortField
     * @param offset
     * @param limit
     * @return
     */
    List<Member> getMembers(Search search, int offset, int limit);

    /**
     * Counts a list of members that match the specified search criteria
     *
     * @param searchTerm
     * @param sortField
     * @return
     */
    int countMembers(Search search);

    /**
     * Gets a member that matches the specified identifier
     *
     * @param memberId
     * @return
     */
    Member getMemberById(String memberId);

    /**
     * Deactivates a member along with all he data associated to it. This member
     * will never be accessible on the UI
     *
     * @param member
     * @throws ValidationFailedException
     */
    void delete(Member member) throws ValidationFailedException;
    
    /**
     * Deactivates a member along with all he data associated to it. This member
     * will never be accessible on the UI
     *
     * @param member
     * @throws ValidationFailedException
     */
    void block(Member member,String blockNotes) throws ValidationFailedException,OperationFailedException;

     void unblock(Member member, String unblockNotes) throws ValidationFailedException, OperationFailedException ;
    /**
     * Gets a member that matches the specified identifier
     *
     * @param phoneNumber
     * @return
     */
    Member getMemberByPhoneNumber(String phoneNumber);

    /**
     * Gets a member that matches the specified identifier
     *
     * @param user
     * @return
     */
    Member getMemberByUserAccount(User user);

    /**
     *
     * @param member
     * @return
     * @throws Exception
     */
    Member activateMemberAccount(Member member) throws Exception;

    /**
     *
     * @param email
     * @return
     */
    Member getMemberByEmail(String email);
}
