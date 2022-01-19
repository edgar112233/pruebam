import { Text, View , NativeModules } from "react-native";
import { Button } from "react-native";
import ToastModule from './ToastModule';
import { APP_LIST } from './app-list';
 



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

  const showAppsInstalled = async (idx, app, pkg) => {
    var installed = await ToastModule.isPackageInstalled(pkg)
    console.log(idx, app, ": ", installed)
  }
  const handleClick1 = async () => {
    //ToastModule.muerte()
    
   
   /* let appCheckResultsPKG = [],
    checkCounterPKG = 0;
    Object.keys(APP_LIST);
      .forEach((d, idx) => {
          checkCounterPKG++;
            AppInstalledChecker
            .isAppInstalledAndroid(d)
            then((isInstalled) => {
              checkCounterPKG--;
                      appCheckResultsPKG.push({name: d, isInstalled: isInstalled, idx: idx});
                        if (checkCounterPKG === 0) {
                            this.renderListPKG(appCheckResultsPKG);
                        }
                    });
            });*/
    Object.keys(APP_LIST).forEach((d,idx) => {
      showAppsInstalled(idx, d, APP_LIST[d].pkgName)      
    });
    //var esta = await ToastModule.isPackageInstalled('com.google.android.youtube');
  } 
  
  const getAplications = async () => {
    //var installed = await ToastModule.isPackageInstalled(pkg)
    var apps = await ToastModule.getApps()
    console.log(apps)
  }
  const alirun = async () => {
    console.log("alirun is")
    try{
      console.log("entro al alirun")
      var run = await ToastModule.killbypackage("com.pruebam")
      console.log(run)
    }catch(e){
      console.log("hizo catch")
      console.log(e)
  }
  }

  return (
      <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
        <Text>Home!</Text>
        <Button  title="prueba" onPress={handleClick}/>
        <Button  title="kill" onPress={handleClick1}/>
        <Button  title="getApps" onPress={getAplications}/>
        <Button  title="aliisrun" onPress={alirun}/>
      </View>
    
  );
}

