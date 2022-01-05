package org.pahappa.systems.core.services.impl;

import java.util.List;

import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Search;
import org.apache.commons.text.WordUtils;
import org.pahappa.systems.core.services.LookUpService;
import org.pahappa.systems.models.LookUpField;
import org.pahappa.systems.models.LookUpValue;
import org.sers.webutils.model.exception.OperationFailedException;

@Service
@Transactional
public class LookUpServiceImpl extends GenericServiceImpl<LookUpField> implements LookUpService {

  

    @Override
    public LookUpField saveInstance(LookUpField lookUpField) throws ValidationFailedException {
        LookUpField lookUpFieldWithSameName = getLookUpFieldByName(lookUpField.getName());
        if (lookUpFieldWithSameName != null && !lookUpField.getId().equals(lookUpFieldWithSameName.getId())) {
            throw new ValidationFailedException("Dataset with same name exists");
        }
        lookUpField.setName(WordUtils.capitalize(lookUpField.getName()));
       return super.save(lookUpField);
    }
 
    @Override
    public List<LookUpField> getLookUpFields(Search search, int offset, int limit) {
        search.setMaxResults(limit);
        search.setFirstResult(offset);
        return super.search(search);
    }

    @Override
    public int countLookUpFields(Search search) {
        return super.count(search);
    }

    @Override
    public LookUpField getLookUpFieldById(String lookUpFieldId) {
        return super.searchUniqueByPropertyEqual("id", lookUpFieldId, RecordStatus.ACTIVE);
    }

   

    @Override
    public LookUpField getLookUpFieldByName(String name) {
        return super.searchUniqueByPropertyEqual("name", name, RecordStatus.ACTIVE);
    }

    @Override
    public LookUpValue getLookUpValueById(String id) {
        return null;
    }

    @Override
    public LookUpValue findValueInLookUp(String lookUpFieldName, String lookUpValueName) {
        
        LookUpField lookUpField= getLookUpFieldByName(lookUpFieldName);
        for (LookUpValue lookUpValue : lookUpField.getLookUpValues()) {
            if (lookUpValue.getName().equalsIgnoreCase(lookUpValueName.trim())) {
                return lookUpValue;
            }
        }
        
        return null;
    }

    @Override
    public boolean isDeletable(LookUpField entity) throws OperationFailedException {
        return true;
    }

 
}
