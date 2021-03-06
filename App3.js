import React, {Component} from 'react'
import {AppState, Text} from 'react-native'

class App3 extends Component {

    state = {
        appState: AppState.currentState
    }

    componentDidMount() {
        AppState.addEventListener('change', this._handleAppStateChange);
        console.log('componentDidMount')
    }

    componentWillUnmount() {
        AppState.removeEventListener('change', this._handleAppStateChange);
        console.log('componentWillMount')
    }

    _handleAppStateChange = (nextAppState) => {
        if (this.state.appState.match(/inactive|background/) && nextAppState === 'active') {
          console.log('App has come to the foreground!')
        }
        else{
            console.log('Background')
        }
        this.setState({appState: nextAppState});
    }

    render() {
        return (
          <Text>Current state is: {this.state.appState}</Text>
        );
    }

}

export default App3;