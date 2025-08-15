# Munchables

## Team Members

- Amanda Li (amansdali) implementing user story 2
- Emilia Ma (emtato) implementing user story 3 (john pork 🐷)
- Janya Singh (JanyaSingh) implementing user story 4
- Spence Saghatelyan (wormonahstring) implementing user story 5; devices: S, Saghatelyan Ani Lilli
- Kate Shen (kat3sjy) implementing user story 6

## Project Summary
- Domain: Social Media App for Foodies
- Summary: Munchables is a social media app for foodies everywhere. Users can create an account and log in to a world of all things food! Make posts, join clubs, explore restaurants, connect with like-minded peers, and more!

## Table of Contents
1. [Features of The Software](#features-of-the-software)
2. [Installation Instructions](#installation-instructions)
3. [Usage Guide](#usage-guide)
4. [License](#license)
5. [Feedback](#feedback)
6. [Contributions](#contributions)

## Features of The Software

### User Story 1
Doug Ford is a foodie who just heard about this app and wants to create an account.
- Sign up
- Log in
### User Story 2
Caf Feine wants to manage her account to personalize it to her needs; in the edit profile page, she changes her display name to her nickname, “Java,” adds a fun bio, a new profile picture, and updates a list of things she is interested in. In the settings page, she changes her account's privacy to private, disables notifications, and changes her password.​ In the following page, she scrolls through a list of accounts she is following, and a list of accounts she has requested to follow. She enters her friend's username and sends a follow
request, which her friend soon accepts.​ Finally, she returns to her profile to view her new account information displayed.​

- Edit Profile and Settings
    - Edit profile/save changes
    - Change account privacy​
    - Enable/disable notifications​
    - Change password
    - Delete account​
    - Logout​

<p align="center">
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/54f883db-6063-4d34-9bfa-a5a20be755e1" />
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/a5840b6e-7209-4f77-9250-7ee055b5da71" />
</p>

- View profile:​
    - View personal profile​
    - View other users' profiles​

<p align="center">
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/c736a5a1-dcf5-4b12-847d-05b8f2732797" />
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/4f611ee9-539e-44c2-9750-31b05e3a2519" />

</p>

- Manage followers/following related use cases:​
    - Follow/unfollow​
    - Send/accept follow request​
    - Delete follower​

<p align="center">
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/deb2401c-c6b8-4051-9daf-597e1d2dd4d1" />
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/ba24e80c-6c20-4a8b-b54a-62cd27d7bb70" />
</p>

### User story 3

John Pork has recently been disconcerted about how much processed food he eats and wants to find healthier but still delicious recipes that suit his tastes. He uses this app to analyze the recipes he picks, to quickly and easily evaluate his choices and balance between flavours, cuisines, and health. The app uses Spoonacular API to analyze the nutritional value of the recipes, which takes in a string input of the recipe, then outputs data such as the recipe’s nutrition in JSON format. John then experiments with the recipes, posting his own alterations.

Use cases:
- Analyze recipe (API) 

- Browse recipes and posts 

- Interactions with posts: 

    - Commenting on posts
  - Liking posts
   - Creating new posts
  - saving posts and comments and likes to database
  - fetching posts, likes and comments from database
    
(homepage view, display posts, fetch posts)
<img width="1206" height="739" alt="image" src="https://github.com/user-attachments/assets/0df8e097-8150-4fb9-917c-766c011ef027" />
(full post view, comment, save comment to database)
<img width="1507" height="910" alt="image" src="https://github.com/user-attachments/assets/aeac076b-7e8a-461c-b101-459d615db1f1" />
(create new post view, save post to database)
<img width="1434" height="882" alt="image" src="https://github.com/user-attachments/assets/bab91924-794d-41ac-ab5f-863a4b645341" />

### User Story 5

Del Icious is new in town and wants to familiarize herself with the restaurants around her, so that she can show her friends when they visit. Meanwhile, Del wants to make sure to find the best restaurant in the city, checking every review and comment relating to the business. During her spare time, Del goes out to check out the restaurants, noting their location on the app, and later writes her own reviews. She has a preference for inexpensive foods, with vegetarian options, to compensate for some of her friends’ needs.

Main Topic: Restaurant list, Map View, Reviews​
- Map [Uses Google Places API (New)]
- Find restaurants based on location, preference, and dietary needs [Uses Google Places API (New)]
- Browsing reviews
- Post review
- Saving reviews in the database (works locally)
  
Given the preferred tags (cuisine) and location of the user, a list of restaurants show in the explore view. If none is provided, the restaurants will still show based on the user's IP.​

Associated Use Case: ​

- Create review;​ creates a new review, a lot similar to creating a new post

- Fetch review;​ fetches the review from the database [Uses CSC207 Grade APIs]

- Search restaurant; searches restaurants based on cuisine (ArrayList<String>) and location (String) [Uses Google Places API (New)]

Navigating each component:

Explore View: After logging in, click the explore button (on the bottom, 2nd button from the left)

<img width="309" height="88" alt="Image" src="https://github.com/user-attachments/assets/0220a473-6b89-41cc-902e-8faf822d3443" />

<img width="1782" height="991" alt="Image" src="https://github.com/user-attachments/assets/186eabe2-4bda-4680-ae97-8415410f39cc" />

In the left column, the user can see a list of restaurants based on their location.
<img width="587" height="795" alt="Image" src="https://github.com/user-attachments/assets/a828235c-2990-4a87-838c-6517a8e46245" />

Each restaurant has 3 buttons:

<img width="561" height="119" alt="Image" src="https://github.com/user-attachments/assets/2a8cab5f-f4ec-4b11-874e-196862c903b4" />

- Click "View Map"

  You will see a map panel with an image and description of the restaurant
  
  <img width="993" height="839" alt="Image" src="https://github.com/user-attachments/assets/e922c7ca-aa0e-4d46-bae9-6774c090007e" />
  
    - Click "Open in Google Maps"
    <img width="1776" height="895" alt="Image" src="https://github.com/user-attachments/assets/28364471-b74e-463e-a29e-2f5ac6f04369" />
    
    - Click "Open Website"
    <img width="1861" height="974" alt="Image" src="https://github.com/user-attachments/assets/646187e5-5737-4fc0-943b-c7db7e4b605e" />
    
    - Click "Close"

      It goes back to the explore page

- Click "Website"

  It is the same as "View Map" -> "Open Website"


- Click "Post Review"

  You will see an pop-up for user's to fill in a review of the restaurant
  
    - Empty review
  <img width="771" height="539" alt="Image" src="https://github.com/user-attachments/assets/04755745-40cd-40ac-bc9e-e29586247d78" />
  
    - Incomplete review
  <img width="766" height="541" alt="Image" src="https://github.com/user-attachments/assets/f00d86c1-88b0-4d05-9b82-c84850f6d96f" />
  
    - Complete review

   <img width="768" height="539" alt="Image" src="https://github.com/user-attachments/assets/df52b3d8-0b6a-4dc9-96b6-1d41522fe015" />

      
  If the review is empty or incomplete, the review will not submit.
  
    <img width="370" height="149" alt="Image" src="https://github.com/user-attachments/assets/dd920519-355c-490b-995a-54754c5f4c05" />


  If the review is complete, the review will submit.
  
    <img width="326" height="144" alt="Image" src="https://github.com/user-attachments/assets/15d36867-3bb8-4b0a-beff-c8aff3d00150" />


### User Story 6

Jean Armstrong runs his own French bakery, and wants to interact with other French pastry chefs to talk about French baked goods and meet more people in the industry.​

He joins a French bakery club and creates posts in the specific French bakery club feed, where he can make posts tailored to French pastries and users interested in the same topic. He also scrolls through the club feed to view and interact with posts that other members in the club have made.

Main Features:

Create Club: Create a new club with a name, description, profile image, tags​

- Select Club Members: Selecting users from database of existing users to join the club​ from a drop down menu

Delete Club: delete an existing club​

Joining/Deleting Club: the user can join or leave an existing club​ by clicking their respective buttons

Making Announcements and Posts: Users can make an announcement or post through a specific club that will only show up on that specific club's feed. Announcement and general posts are separated into different feeds, while announcement posts will also show up on the general club home page.

Club Homepage View
<img width="1615" height="961" alt="image" src="https://github.com/user-attachments/assets/e03bf1e6-3b1e-4ce1-a367-3644017114d4" />

Club-Specific View
<img width="1729" height="1097" alt="image" src="https://github.com/user-attachments/assets/377386fd-609a-4a7e-8f0f-999be251783e" />

Create Club View
<img width="1329" height="507" alt="image" src="https://github.com/user-attachments/assets/d10b7a69-db76-4c8a-99c7-1219da6a5b9c" />

## Installation Instructions

### Required Software
Before installing, make sure you have the following:
1. Java Development Kit (JDK 11 or higher)
2. Apache Maven (3.6.0 or higher)
- Download: https://maven.apache.org/download.cgi
- Installation guide: https://maven.apache.org/install.html
3. Git (If cloning the repository)

### Installation Steps
Cloning From Github:
1. Open a terminal
2. Enter Commands:
    - git clone https://github.com/emtato/csc207-project.git
    - cd csc207-project

Downloading as ZIP File:
1. Go to the GitHub repository page.
2. Click Code → Download ZIP.
3. Extract the ZIP file.
4. Open a terminal
5. Navigate to the extracted folder:
    -  cd *path to extracted folder*

### Building and Running the Project
1. Use Maven to build the project and download dependencies:
    - In the terminal run: mvn clean install
2. Run the application:
    - mvn exec:java -Dexec.mainClass="app.Munchables"

## Usage Guide
Simply sign up with a new account or log in to an existing one, and explore the app however you'd like.

Fill in the following text fields to sign up, then click on "sign up"
<img width="2215" height="1096" alt="image" src="https://github.com/user-attachments/assets/796b619c-1344-47bf-adfd-ee1e3db9fdac" />

After signing up, click on the "log in" button to proceed to the login page and then type in the same credentials you used to sign up. Then click on "log in" to gain access to the rest of the program.
Log in page:
<img width="2210" height="972" alt="image" src="https://github.com/user-attachments/assets/2b7c94a6-186a-4d48-8bcc-a4fc76eb481a" />

Homepage:
<img width="1492" height="763" alt="image" src="https://github.com/user-attachments/assets/47b03b7f-7925-4e41-a38e-9de95da06ab2" />

You did it! Feel free to explore the rest of the app :))



## License
Creative Commons Zero v1.0 Universal

## Feedback
- Feedback is currently not accepted. A form for providing feedback will be provided in the future.

## Contributions
- Contributions are closed for users who are not on the team.
