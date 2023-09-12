interface DesignInformation {
  projectConfigs: Record<string, string>
  flows: DFlow[]
  components: Record<string, DComponent>
  layouts: DLayout[]
  libraries: DLibrary[]
}

interface DConfig {
  key: string
  value: string
}

interface DComponent {
  name: string
  child?: DComponent[]
  configs?: Record<string, string>
}

interface DSee {
  componentName: string
  data: string
}

interface DDo {
  uiEvent: string
  componentName: string
  data: string
}

interface DReact {
  sceneName: string
  reactAction: string
  reactComponentName: string
  animateName: string
  reactComponentData: string
}

interface DInteraction {
  see: DSee
  do: DDo
  react: DReact[]
}

interface DFlow {
  interactions: DInteraction[]
  flowName: string
}

interface DLayout {
  layoutName: string
  layoutRows: DLayoutRow[]
}

interface DLayoutRow {
  layoutCells: DLayoutCell[]
}

interface DLayoutCell {
  componentName: string
  layoutSize: string
  comment: string
}

interface DProperty {
  key: string
  value: string
}

interface LibraryPreset {
  key: string
  value: string
  presetCalls?: PresetCall[]
  subProperties?: DProperty[]
  inheritProps?: DProperty[]
}

interface PresetCall {
  name: string
  preset: string
}

interface DLibrary {
  name: string
  presets: LibraryPreset[]
}
