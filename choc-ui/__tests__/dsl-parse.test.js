describe('DSL Parser', () => {
  it('should render the components', () => {
    // ------------------------------------------------------
    // |      NavComponent(12x)                             |
    // ------------------------------------------------------
    // |    Text(6x)                     |   Empty(4x)      |
    // ------------------------------------------------------
    // | Avatar(2x)  | Date(2x)          |   Empty(6x)      |
    // ------------------------------------------------------
    // | CardMedia(8x)                                      |
    // ------------------------------------------------------
    // | Typography(12x)                                    |
    // ------------------------------------------------------
    // | FooterComponent(12x)                               |
    // ------------------------------------------------------
    const dslString =
      '{"projectConfigs":{},"flows":[],"components":{},"layouts":[{"layoutName":"","layoutRows":[{"layoutCells":[{"componentName":"NavComponent","layoutInformation":"12x","normalInformation":""}]},{"layoutCells":[{"componentName":"Text","layoutInformation":"6x","normalInformation":""},{"componentName":"Empty","layoutInformation":"4x","normalInformation":""}]},{"layoutCells":[{"componentName":"Avatar","layoutInformation":"2x","normalInformation":""},{"componentName":"Date","layoutInformation":"2x","normalInformation":""},{"componentName":"Empty","layoutInformation":"6x","normalInformation":""}]},{"layoutCells":[{"componentName":"CardMedia","layoutInformation":"8x","normalInformation":""}]},{"layoutCells":[{"componentName":"Typography","layoutInformation":"12x","normalInformation":""}]},{"layoutCells":[{"componentName":"FooterComponent","layoutInformation":"12x","normalInformation":""}]}]}],"libraries":[]}'
    const dsl = JSON.parse(dslString)

    console.log(dsl)
  })
})
