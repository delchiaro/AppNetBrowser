/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  Image,
  Button,
  Alert,
  View, TouchableOpacity
} from 'react-native';

import Communications from 'react-native-communications';


import {
  MKButton, MKColor,
} from 'react-native-material-kit';


				

const onMoreInfoButtonPress = () => {
  //Alert.alert('More info request sending');
  Communications.email(['missingitems.notify@missing.com'],null,'','MissingItems notify #i4621823','Find item #4621823 from user #u1274 in location:')
};

const onAboutButtonPress = () => {
  Alert.alert('MissingItems WebApp: help people to find lost objects.');
};

const MoeInfoButton = MKButton.coloredButton()
  .withText('LET ME KNOW WHERE YOU FIND IT')
  .withBackgroundColor('#2196F3')
  .withOnPress(() => { onMoreInfoButtonPress() })
  .build();

const AboutMyBedgeButton = MKButton.coloredButton()
  .withText('About: MissingItems')
  .withBackgroundColor('#E91E63')
  .withOnPress(() => { onAboutButtonPress() })
  .build();





export default class Main extends Component {
	
  render() {
    return (
		<View style={styles.mainContainer}   > 
		<View style={styles.centralContainer}>


			<View style={styles.headerContainer}>
				<Image source={require('./portafoglio.jpg')} style={styles.image} />
			</View>




			<View style={styles.bodyContainer}>
				<View style={styles.textContainer}>

					<Text style={styles.welcome}>I LOSE MY WALLET</Text>
					<Text style={styles.instructions, styles.textRow}>I lose my wallet that is localized near to you.
						Please help me to find it, call me at my mobile phone or send me a message with GPS location using the buttons below.
					</Text>
					
					<TouchableOpacity onPress={() => Communications.phonecall('0123456789', true)} style={{flexDirection: 'row', }}>
      					<Text style={styles.instructions, styles.textRow}>Phone: </Text>
      					<View style={styles.holder, styles.textRow}>							  
			            <Text style={styles.text}> 0123456789</Text>
			          </View>
			        </TouchableOpacity>

				</View>

				<View style={styles.footerContainer}>
						
					<View style={styles.buttonMoreInfoContainer}>
						<MoeInfoButton/>							
					</View>
					<View style={styles.buttonMoreInfoContainer}>
						<AboutMyBedgeButton/>
					</View>
				</View>

			</View>
			</View>

			
			
      	</View>
      	
    );
  }
}


const styles = StyleSheet.create({

	 mainContainer: {
		flex: 1,
		margin: 4,
		flexDirection: 'column', 
		justifyContent: 'flex-start',
		alignItems: 'center',
		backgroundColor: '#F5FCFF',
	  },
 	centralContainer: {
 		flex: 1,
 		margin: 10,
		flexDirection: 'column', 
		justifyContent: 'flex-start',
		backgroundColor: '#F5FCFF',
	  },

	headerContainer: {
		flexDirection: 'column',
		overflow: 'hidden',
		justifyContent: 'space-between',
		alignItems: 'center',
		backgroundColor: '#F5FCFF',
					borderRadius: 5,

	},
	bodyContainer: {
		flex: 1,
		flexDirection: 'column', 
		justifyContent: 'space-between',
		backgroundColor: '#F5FCFF',
	},



	// HEADER:
		image: {
			height: 350, 
			width: 300,
			resizeMode: 'contain',
			borderRadius: 5,
			//borderRightWidth: 10,
			backgroundColor: '#F5FCFF',
		},




	// BODY:


		  textContainer: {
			flex: 1,
			marginTop: 10,
			flexDirection: 'column', 
			justifyContent: 'flex-start',
			alignItems: 'flex-start',
			backgroundColor: '#F5FCFF',
		  },
		  footerContainer: {
		  	height: 80,
			flexDirection: 'column', 
			justifyContent: 'flex-start',
			backgroundColor: '#F5FCFF',
		  },

		 
		 welcome: {
			fontSize: 20,
			textAlign: 'center',
		  },
		  textRow: {
			marginTop: 5,
		  },
		 instructions: {
			textAlign: 'center',
			color: '#333333',
		  },
		buttonMoreInfoContainer: {
			flex: 1,
			marginLeft: 1,
			marginRight: 1,
	        justifyContent: 'flex-start',
			flexDirection: 'column',
			alignItems: 'stretch',
		},
		buttonMoreInfo: {
			height: 10, width: 10,
		},


	 buttonText: {
		fontSize: 14,
		fontWeight: 'bold',
		color: 'white',
	  },
	  fab: {
		// width: 200,
		// height: 200,
		// borderRadius: 100,
	  },
});

AppRegistry.registerComponent('Main', () => Main);
