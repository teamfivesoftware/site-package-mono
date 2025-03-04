const { app, BrowserWindow } = require('electron');
const path = require('path');

let mainWindow;

app.on('ready', () => {
  mainWindow = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'), // Optional, but useful for secure communication
      nodeIntegration: true, // Allows using Node.js features in the frontend
    },
    icon: path.join(__dirname, 'src/LOGO.icns')
  });

  mainWindow.hide();
  mainWindow.maximize();
  mainWindow.show();
  mainWindow.loadURL('http://localhost:3000'); // Loads the React app
});

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit();
  }
});