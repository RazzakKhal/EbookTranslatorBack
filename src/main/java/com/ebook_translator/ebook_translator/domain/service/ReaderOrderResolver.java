package com.ebook_translator.ebook_translator.domain.service;

import com.ebook_translator.ebook_translator.domain.exception.InvalidEbookFormatException;
import com.ebook_translator.ebook_translator.domain.model.Manifest;
import com.ebook_translator.ebook_translator.domain.model.ManifestItem;
import com.ebook_translator.ebook_translator.domain.model.ReaderOrderItem;
import com.ebook_translator.ebook_translator.domain.model.Spine;
import com.ebook_translator.ebook_translator.domain.model.SpineItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReaderOrderResolver {

    public ReaderOrderResolver() {
    }

    public List<ReaderOrderItem> resolve(Manifest manifest, Spine spine) {
        Map<String, ManifestItem> manifestById = manifest.items().stream()
                .collect(Collectors.toMap(ManifestItem::id, Function.identity()));
        var resolverList = new ArrayList<ReaderOrderItem>(spine.items().size());

        var spinesItems = spine.items();
        for (SpineItem spinesItem : spinesItems) {
            var idRef = spinesItem.idRef();
            var foundManifest = manifestById.get(idRef);

            if (foundManifest == null) {
                throw new InvalidEbookFormatException();
            }
            resolverList.add(new ReaderOrderItem(idRef, foundManifest.href()));
        }

        return resolverList;
    }
}
