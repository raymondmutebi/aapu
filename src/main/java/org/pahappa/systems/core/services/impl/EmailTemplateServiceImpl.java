package org.pahappa.systems.core.services.impl;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.Validate;
import org.pahappa.systems.core.services.EmailTemplateService;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.models.EmailTemplate;
import org.pahappa.systems.constants.TemplateType;

@Service
@Transactional
public class EmailTemplateServiceImpl extends GenericServiceImpl<EmailTemplate> implements EmailTemplateService {
	
	@Override
	public List<EmailTemplate> getTemplates() {
		Search search = new Search();
		search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
		return super.search(search);
	}

	@Override
	public int countTemplates() {
		Search search = new Search();
		search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
		return super.count(search);
	}

	@Override
	public EmailTemplate saveInstance(EmailTemplate emailTemplate)
			throws ValidationFailedException, OperationFailedException {
		Validate.notNull(emailTemplate, "Missing template");
		Validate.notNull(emailTemplate.getTemplateType(), "Missing template type");
		Validate.notNull(emailTemplate.getTemplate(), "Missing template details");
		
		EmailTemplate existingTemplate = getEmailTemplateByType(emailTemplate.getTemplateType());
		if (existingTemplate != null && !Objects.equals(existingTemplate.getId(), emailTemplate.getId()))
			throw new ValidationFailedException(String.format("%s template already exists", emailTemplate.getTemplateType().getName()));

		return super.save(emailTemplate);
	}

	@Override
	public EmailTemplate getEmailTemplateByType(TemplateType templateType) {
		Search search = new Search();
		search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
		if(templateType != null)
			search.addFilterEqual("templateType", templateType);
		search.setMaxResults(1);
		return super.searchUnique(search);
	}

	@Override
	public boolean isDeletable(EmailTemplate entity) throws OperationFailedException {
		return true;
	}
}
