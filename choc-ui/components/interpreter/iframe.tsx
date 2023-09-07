import React, { useState, useEffect, useCallback, useRef } from "react";
import Frame from "react-frame-component";

export const Iframe = ({ content } : { content: string }) => {
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
            // const newHeight = body?.scrollHeight || 0;
            // if (newHeight !== 0) {
            //     setHeight(newHeight);
            // }
        }
    }, []);

    useEffect(() => {
        handleResize();
    }, [handleResize]);

    return (
        <Frame
            ref={iframeRef}
            style={{
                width: `620px`,
                height: `420px`,
                margin: `0 auto`,
            }}
            initialContent={content + `<style>html, body, body > div { height: 400px; width: 600px }</style>`}
            onLoad={handleResize}
        >
        </Frame>
    );
};
