import ToastModule from './ToastModule';
import { APP_LIST } from './app-list';
import React, {useState} from 'react';
import { SafeAreaView, View, FlatList, StyleSheet, Text, StatusBar, Pressable, NativeModules } from 'react-native';
import MaterialCommunityIcons from 'react-native-vector-icons/MaterialCommunityIcons'; //Download

const Item = ({ title }) => {
    const [islock, setLock] = useState(false);

    const onClickItem  = () => {
        //setLock(!islock);
        if(!islock){
            //Desactivar aplicación.
            console.log(islock)
        }
        setLock(!islock);
        //console.log(islock)
    }
    return (
      <Pressable style={styles.item} onPress={onClickItem}>
            <Text style={styles.title}>{title}</Text>
            {/*<MaterialCommunityIcons style={styles.icons} name="lock-open" size={26} />*/}
            { islock ? (
                <MaterialCommunityIcons style={styles.icons} name="lock" color="red"size={26} />
                ) : (
                <MaterialCommunityIcons style={styles.icons} name="lock-open" color="black" size={26} />
            )}
      </Pressable>

      /*<Text style={styles.title}>{title}</Text>
      <MaterialCommunityIcons style={styles.icons} name="lock-open" size={26} />*/
    );
};

const renderSeparator = () => (
    <View
        style={{
        backgroundColor: '##FAFAFA',
        height: 1,
      }}
    />
  );
  

class App2 extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            data: []
        };
    }
   
    componentDidMount() {        
        ToastModule.getApps().then(
          (result) => {
            this.setState({
              isLoaded: true,
              data: result
            });
          },
          (error) => {
            this.setState({
              isLoaded: true,
              error
            });
          }
        )
    }
   
    render() {

      const { error, isLoaded, data } = this.state;

      if (error) {
        return <Text > Error: {error.message}</Text>;

      } else if (!isLoaded) {
            return <Text> Cargando ... </Text>;

      } else {
            const renderItem = ({ item }) => (
                <Item title={item.appName} />
            )
            return (
                <SafeAreaView style={styles.container}>
                    <FlatList
                        data={data}
                        renderItem={renderItem}
                        keyExtractor={item => item.packageName}
                        ItemSeparatorComponent={renderSeparator}
                    />
                </SafeAreaView>  
            );
      }
    }
  }
 
 
const styles = StyleSheet.create({
    container: {
      flex: 1,
      marginTop: StatusBar.currentHeight || 0,
    },
    item: {
      flexDirection: 'row',
      backgroundColor: '#FFFFFF',
      padding: 20,
      marginVertical: 0,
      marginHorizontal: 0,
    },
    title: {
      fontSize: 20,
    },
    icons: {
        marginLeft: 0,
    }
});
  
  export default App2;