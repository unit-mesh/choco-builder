import React, { useState, useEffect, useCallback, useRef } from "react";
import Frame from "react-frame-component";

export const Iframe = ({ content } : { content: string }) => {
    const [height, setHeight] = useState<number>(500);
    // @ts-ignore
    const iframeRef = useRef<HTMLIFrameElement>(null);

    const handleResize = useCallback(() => {
        const iframe = iframeRef.current;
        if (iframe) {
            const iframeDocument = iframe.contentDocument;
            const body = iframeDocument?.body;
            if (body!!) {
                iframeDocument.documentElement.style.height = "400px";
                body.style.height = "400px";
                iframeDocument.documentElement.style.width = "600px";
                body.style.width = "600px";
            }
            const newHeight = body?.scrollHeight || 0;
            if (newHeight !== 0) {
                setHeight(newHeight);
            }
        }
    }, []);

    useEffect(() => {
        handleResize();
    }, [handleResize]);

    return (
        <Frame
            ref={iframeRef}
            style={{
                height: `${height}px`,
            }}
            initialContent={content}
            onLoad={handleResize}
        >
            <div id={'placeholder'} style={{
                height: `${height}px`,
                width: `${height}px`,
            }}></div>
            <div />
        </Frame>
    );
};
