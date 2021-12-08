package org.pahappa.systems.core.services;


import java.util.List;
import org.pahappa.systems.models.EmailTemplate;
import org.pahappa.systems.constants.TemplateType;


public interface EmailTemplateService extends GenericService<EmailTemplate>{
	/*
	 * Retrieve all email templates
	 */
    List<EmailTemplate> getTemplates();
    
	/*
	 * Get template by type
	 */
    EmailTemplate getEmailTemplateByType(TemplateType templateType);

	/*
	 * Count templates
	 */
    int countTemplates();
}
