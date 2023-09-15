package cc.unitmesh.rag.document;

import java.io.InputStream;

public interface DocumentParser {

    String DOCUMENT_TYPE = "document_type";

    Document parse(InputStream inputStream);
}
