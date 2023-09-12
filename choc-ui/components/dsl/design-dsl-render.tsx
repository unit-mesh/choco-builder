import React from 'react'

interface LayoutCellProps {
  componentName: String
  layoutSize: String
  comment: String
}

const LayoutCell = ({
  componentName,
  layoutSize,
  comment
}: LayoutCellProps) => {
  let width = layoutSize.replace('x', '')
  // if comment is not empty, then after commentName
  let displayName = componentName
  if (comment) {
    displayName = `${displayName} (${comment})`
  }

  return (
    <div
      className={`w-${width}/12 px-4 py-2 bg-violet-500 rounded-lg shadow-lg text-center text-white`}
    >
      {displayName}
    </div>
  )
}

const LayoutRow = ({ layoutCells }: { layoutCells: DLayoutCell[] }) => (
  <div className="flex">
    {layoutCells.map((cell, index) => {
      return <LayoutCell {...cell} key={index} />
    })}
  </div>
)

const Layout = ({ layoutRows }: { layoutRows: DLayoutRow[] }) => (
  <div className="mx-auto max-w-md overflow-hidden rounded-xl bg-white shadow-md space-y-1">
    {layoutRows.map((row, index) => (
      <LayoutRow key={index} {...row} />
    ))}
  </div>
)

export const DesignDslRender = ({ dsl }: { dsl: DesignInformation }) => {
  console.log('DesignDslRender')
  console.log(dsl)

  return (
    <div>
      <Layout layoutRows={dsl.layouts[0].layoutRows} />
    </div>
  )
}
