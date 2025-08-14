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

## License
Creative Commons Zero v1.0 Universal

## Feedback
- Feedback is currently not accepted. A form for providing feedback will be provided in the future.

## Contributions
- Contributions are closed for users who are not on the team.
