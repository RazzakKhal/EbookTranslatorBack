package com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;

import java.util.List;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
public class ManifestXml {

    @XmlElement(name = "item", namespace = "http://www.idpf.org/2007/opf")
    private List<ManifestItemXml> items;
}
