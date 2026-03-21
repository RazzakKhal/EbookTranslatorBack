package com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.Getter;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
public class SpineItemRefXml {

    @XmlAttribute(name = "idref")
    private String idRef;
}
