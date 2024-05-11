package com.martzatech.vdhg.crmprojectback.application.helper;

import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

import static com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants.EXTENSION_REGEX;
import static com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants.EXTENSION_REGEX_DOC;

@Slf4j
public class FileHelper {
    private static final List<String> extensions = List.of("png","PNG","jpeg","JPEG","jpg","JPG",
            "gif","GIF","webp","WEBP","pdf","PDF","docx","DOCX","doc","DOC","txt","TXT","xlsx","XLSX","xls","XLS");
    private static final Pattern EXTENSION_PATTERN = Pattern.compile(EXTENSION_REGEX);
    public static void validateExtensionFile(final String extension, String extensionFile) {
        log.debug("Extension is comparing : ", extensionFile);

        if (StringUtils.isEmpty(extension) && StringUtils.isEmpty(extensionFile)) {
            throw new BusinessRuleException("The extension is mandatory");
        }

        if (!EXTENSION_PATTERN.matcher(extension).find()) {
            throw new BusinessRuleException("The extension is not valid. Should be like: " + EXTENSION_REGEX);
        }

        if (!extensionFile.equals(extension)) {
            throw new BusinessRuleException("The extension is not compared with the original of the file please check it");
        }

        if (!extensions.contains(extensionFile)) {
            throw new BusinessRuleException("The extension is not valid. Should be like: " + EXTENSION_REGEX_DOC);
        }
    }
}
