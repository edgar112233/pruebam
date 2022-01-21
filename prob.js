
  state = { networkName: null, isConnected: false }
  componentDidMount() {
    this.getNetWorkState();
  }

  getNetWorkState = () => {
    connectionStatusModule.checkConnectionStatus(this.getInfo)
    setTimeout(
      () => this.getNetWorkState(),
      2000
    )
  }

  getNetWorkState = () => {
    connectionStatusModule.checkConnectionStatus(( networkName, isConnected) => this.setState({ networkName, isConnected }))
    setTimeout(
      () => this.getNetWorkState(),
      2000
    )
  }



  getInfo = ( networkName, isConnected) => this.setState({ networkName, isConnected });