package com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.Getter;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
public class ManifestItemXml {

    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "href")
    private String href;

    @XmlAttribute(name = "media-type")
    private String mediaType;
}
