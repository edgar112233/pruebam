import { Linking, Platform } from 'react-native';
import { APP_LIST } from './app-list';
import CheckPackageInstallationModule from './ToastModule';

class AppInstalledChecker {

    static getAppList() {
        return Object.keys(APP_LIST);
    }
}