var modules={};
//only react-native-threads here, other modules should be added on demand
modules['react-native-threads']=require('react-native-threads');
//should be the same as in index.ios.js
var devHost = '192.168.2.19'
require('worker-bridge').withModules(modules).loadApp(devHost);
