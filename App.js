import { Text, View , NativeModules } from "react-native";
import { Button } from "react-native";
import ToastModule from './ToastModule';



export default function App() {


  const handleClick = async () => {
    try{
    ToastModule.show('Awesome', ToastModule.LONG); 
    } catch(e) { 
      console.error(e); 
    }
  }   
    

  return (
      <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
        <Text>Home!</Text>
        <Button  title="prueba" onPress={handleClick}/>
      </View>
    
  );
}

