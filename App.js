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
    try{
      ToastModule.pcallback(
        'juan53',
        'testLocation',
        (msg) => {
          console.log(msg);
        },
        (x,y,hola,Name)=>{
          console.log('equis:'+x +'y:'+y+'este es hola:'+hola+'name:'+Name);
        }
      );
    } catch(e){
      console.error(e);
    }


    try{
      var {x, y, APPs, Name} = await ToastModule.ppromise(
        'juan77',
        'testLocation');

        console.log('equis con promise:'+x +'y:'+y+'name:'+Name);
        console.log('las apps instalada:',APPs)
        
    } catch(e){
      console.error(e);
    }
  } 
  const handleClick1 = async () => {
    ToastModule.muerte()
  } 
    

  return (
      <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
        <Text>Home!</Text>
        <Button  title="prueba" onPress={handleClick}/>
        <Button  title="kill" onPress={handleClick1}/>
      </View>
    
  );
}

