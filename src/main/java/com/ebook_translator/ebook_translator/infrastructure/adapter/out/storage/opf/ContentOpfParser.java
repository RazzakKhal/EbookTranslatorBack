package com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf;

import com.ebook_translator.ebook_translator.domain.exception.InvalidEbookFormatException;
import com.ebook_translator.ebook_translator.domain.model.ParsedContentOpf;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.dto.ContentOpfXml;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.mapper.ManifestXmlMapper;
import com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.mapper.SpineXmlMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.nio.file.Path;

public class ContentOpfParser {

    private static final JAXBContext JAXB_CONTEXT = createJaxbContext();
    private final ManifestXmlMapper manifestXmlMapper;
    private final SpineXmlMapper spineXmlMapper;

    public ContentOpfParser(ManifestXmlMapper manifestXmlMapper, SpineXmlMapper spineXmlMapper) {
        this.manifestXmlMapper = manifestXmlMapper;
        this.spineXmlMapper = spineXmlMapper;
    }

    private static JAXBContext createJaxbContext() {
        try {
            return JAXBContext.newInstance(ContentOpfXml.class);
        } catch (JAXBException exception) {
            throw new IllegalStateException("Unable to initialize JAXB context for content.opf parsing", exception);
        }
    }

    public ParsedContentOpf parse(Path contentOpfPath) {
        ContentOpfXml contentOpf = readContentOpf(contentOpfPath);
        return new ParsedContentOpf(
                manifestXmlMapper.map(contentOpf.getManifest()),
                spineXmlMapper.map(contentOpf.getSpine())
        );
    }

    private ContentOpfXml readContentOpf(Path contentOpfPath) {
        try {
            Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            return (ContentOpfXml) unmarshaller.unmarshal(contentOpfPath.toFile());
        } catch (JAXBException exception) {
            throw new InvalidEbookFormatException();
        }
    }
}
