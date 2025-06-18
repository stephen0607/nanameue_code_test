# 🌐 nanameue, Inc. Android App Test‬

⚠️ Build Issues?

If you're unable to build or run the project due to dependency conflicts or other errors, you can still try out the app using the pre-built APK included in this repository.

📦 The APK file is located at:

```/nanameue_code_test/app/test_build.apk```

You can manually install it on your Android device to test the app without needing to build it locally.


## 🚀 Getting Started

```bash
git clone https://github.com/stephen0607/nanameue_code_test.git
```

### 🔐 Firebase Setup

> 🔒 `google-services.json` is excluded from this repository for security.

To run the app:
1. I will send you the `google-services.json` file via email.
2. Place it in your local project under:  
   ```
   /nanameue_code_test/app/google-services.json
   ```

---

## ✨ Features

### 🔐 Authentication
- Sign up with **email** & **password**
- Input validation:
  - Valid email format
  - Minimum 8-character password
  - Confirm password must match
- Firebase error handling with dialog (e.g. email already used, invalid login)
- Redirect to timeline after successful registration/login
- `Login` & `Sign up` button becomes enabled once input is valid


### 🕒 Timeline
- Fetch and display the **latest 10 posts** from Firebase Firestore
- Each post shows:
  - User display name (fallback to email)
  - First 9 digits of UID
  - Optional **text**, **image**, and **timestamp**
- Floating Action Button to create a new post

### 📝 Create Post
- Shows current user name (or email if name not set)
- Allows:
  - Text only
  - Image only
  - Both
- `Post` button becomes enabled once input is valid
- If image is included:
  - Uploads to Firebase Storage
  - On success, saves post object with image URL to Firestore
- Error dialog shown if upload or post fails
- Redirects back to timeline with auto-refresh on success

---

## 🛠 Tech Stack

| Layer           | Technology                         |
|----------------|-------------------------------------|
| **UI**         | Jetpack Compose, Material3          |
| **State**      | StateFlow, collectAsState           |
| **Navigation** | Jetpack Navigation Compose          |
| **DI**         | Koin                                |
| **Backend**    | Firebase Auth, Firestore, Storage   |
| **Image**      | Coil                                |
| **Testing**    | JUnit, MockK, Coroutine Test        |

---

## 🧪 Testing

Unit tests are included for business logic (UseCase) using:
- junit, mockk, kotlinx-coroutines-test
---

## 📸 Screenshots

| Login | Sign Up | Profile |
|-------|---------|---------|
| <img src="https://github.com/user-attachments/assets/0d1276bb-3bb7-4403-81a2-0c25a2bc1e90" width="200"/> | <img src="https://github.com/user-attachments/assets/02a55b20-f4a1-47c1-941e-86fd6878e4c0" width="200"/> | <img src="https://github.com/user-attachments/assets/acc89715-212c-49e3-a884-812a15cc8a02" width="200"/> |


| Create Post | Timeline |
|----------|-------------|
| <img src="https://github.com/user-attachments/assets/5a7c9630-4c6c-43e1-9cfd-2cea2c1051be" width="200"/> | <img src="https://github.com/user-attachments/assets/28eabd87-fe1e-4466-9bb3-4c96e20316d4" width="200"/> |

---

## 🔐 Test Account

You can use the following credentials to log in:

- **Email**: `testuser@gmail.com`  
- **Password**: `11111111`

Alternatively, feel free to sign up with your own email and password.

## 👨‍💻 Author

**Alamdin Stephen**  
