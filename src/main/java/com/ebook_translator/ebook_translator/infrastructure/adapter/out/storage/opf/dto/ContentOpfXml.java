package com.ebook_translator.ebook_translator.infrastructure.adapter.out.storage.opf.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "package", namespace = "http://www.idpf.org/2007/opf")
public class ContentOpfXml {

    @XmlElement(name = "manifest", namespace = "http://www.idpf.org/2007/opf")
    private ManifestXml manifest;

    @XmlElement(name = "spine", namespace = "http://www.idpf.org/2007/opf")
    private SpineXml spine;
}
