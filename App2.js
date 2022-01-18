import ToastModule from './ToastModule';
import { APP_LIST } from './app-list';
import React, {useState} from 'react';
import { SafeAreaView, View, FlatList, StyleSheet, Text, StatusBar, Pressable, NativeModules, Image } from 'react-native';
import MaterialCommunityIcons from 'react-native-vector-icons/MaterialCommunityIcons'; //Download

const Item = ({ title, icon, pkgName  }) => {
    const [islock, setLock] = useState(false);

    const onClickItem  = () => {
        //setLock(!islock);
        if(!islock){
            //Desactivar aplicación.
            console.log(title)
            console.log(pkgName)
        }
        setLock(!islock);
        //console.log(islock)
    }
    return (
      <Pressable style={styles.item} onPress={onClickItem}>
              <Image
                style={styles.logo}
                source={{
                uri: 'data:image/png;base64,'.concat(icon)
              }}
              />
              <View style={styles.view}>
                <Text style={styles.title}>{title}</Text>
                {/*<MaterialCommunityIcons style={styles.icons} name="lock-open" size={26} />*/}
                { islock ? (
                    <MaterialCommunityIcons style={styles.icons} name="lock" color="red"size={26} />
                    ) : (
                    <MaterialCommunityIcons style={styles.icons} name="lock-open" color="black" size={26} />
                )}
            </View>
      </Pressable>
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
                <Item title={item.appName} icon={item.icon} pkgName = {item.packageName} />
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
      //padding: 5,
      marginVertical: 0,
      marginHorizontal: 0,
      justifyContent: 'flex-start',
      //alignItems: 'center',
    },
    title: {
      fontSize: 15
    },
    icons: {
        marginLeft: 0,
    },
    logo: {
      width: 50,
      height: 50,
    },
    view: {
      padding: 5,
      flexDirection: 'row',
      justifyContent: 'center'
    }
});
  
  export default App2;