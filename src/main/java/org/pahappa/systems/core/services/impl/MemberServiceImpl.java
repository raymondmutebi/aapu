package org.pahappa.systems.core.services.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.pahappa.systems.models.Member;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.utils.CustomAppUtils;
import org.pahappa.systems.core.services.MemberService;
import org.sers.webutils.model.exception.OperationFailedException;

@Service
@Transactional
public class MemberServiceImpl extends GenericServiceImpl<Member> implements MemberService {

   

    @Override
    public Member saveInstance(Member manufacturer) throws ValidationFailedException {

        if (StringUtils.isBlank(manufacturer.getPhoneNumber())) {
            throw new ValidationFailedException("Phonenumber should not be empty");
        }

        String validatedPhoneNumber = CustomAppUtils.validatePhoneNumber(manufacturer.getPhoneNumber());

        if (validatedPhoneNumber == null) {
            throw new ValidationFailedException("Invalid Phone number");
        }
        manufacturer.setPhoneNumber(validatedPhoneNumber);

        Member existingWithPhone = getManufacturerByPhoneNumber(manufacturer.getPhoneNumber());

        if (existingWithPhone != null && !existingWithPhone.getId().equals(manufacturer.getId())) {
            throw new ValidationFailedException("A manufacturer with the same phone number already exists!");
        }

        return super.merge(manufacturer);
    }

    @Override
    public Member saveOutsideContext(Member manufacturer) throws ValidationFailedException {

        if (StringUtils.isBlank(manufacturer.getPhoneNumber())) {
            throw new ValidationFailedException("Phonenumber should not be empty");
        }

        String validatedPhoneNumber = CustomAppUtils.validatePhoneNumber(manufacturer.getPhoneNumber());

        if (validatedPhoneNumber == null) {
            throw new ValidationFailedException("Invalid Phone number");
        }
        manufacturer.setPhoneNumber(validatedPhoneNumber);

        Member existingWithPhone = getManufacturerByPhoneNumber(manufacturer.getPhoneNumber());

        if (existingWithPhone != null && !existingWithPhone.getId().equals(manufacturer.getId())) {
            throw new ValidationFailedException("A manufacturer with the same phone number already exists!");
        }

        return super.mergeBG(manufacturer);
    }

    @Override
    public Member getManufacturerByPhoneNumber(String phoneNumber) {
        Search search = new Search();
        search.addFilterEqual("phoneNumber", phoneNumber);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        List<Member> manufacturers = super.search(search);
        if (manufacturers.isEmpty()) {
            return null;
        }
        return manufacturers.get(0);
    }

    @Override
    public List<Member> getManufacturers(Search search, int offset, int limit) {
        search.setFirstResult(offset);
        search.setMaxResults(limit);
        return super.search(search);
    }

    @Override
    public int countManufacturers(Search search) {
        return super.count(search);
    }

    @Override
    public Member getManufacturerById(String manufacturerId) {
        return super.searchUniqueByPropertyEqual("id", manufacturerId, RecordStatus.ACTIVE);
    }

   

    @Override
    public boolean isDeletable(Member entity) throws OperationFailedException {
        return true;
    
    }

   
}
