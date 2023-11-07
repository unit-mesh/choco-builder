"use client"

import LiveEditor from "@/components/editor/live-editor";
import {cn} from "@/lib/utils";

export default function IndexPage() {
  return <div className={cn('pb-[200px] pt-4 md:pt-10')}>
    <LiveEditor />
  </div>
}
