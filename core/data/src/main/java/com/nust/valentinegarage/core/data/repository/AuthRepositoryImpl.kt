package com.nust.valentinegarage.core.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nust.valentinegarage.core.data.dto.UserDto
import com.nust.valentinegarage.core.data.mapper.toDomain
import com.nust.valentinegarage.core.domain.repository.AuthRepository
import com.nust.valentinegarage.core.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun login(email: String, pass: String): Result<User> = try {
        val result = auth.signInWithEmailAndPassword(email, pass).await()
        val uid = result.user?.uid ?: throw Exception("Login failed: UID is null")
        
        // Fetch user metadata from Firestore
        val userDoc = firestore.collection("users").document(uid).get().await()
        val userDto = userDoc.toObject(UserDto::class.java) 
            ?: throw Exception("User profile not found in database")
        
        val user = userDto.toDomain().copy(id = userDoc.id)
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override fun getCurrentUser(): Flow<User?> = callbackFlow {
        var userDocListener: com.google.firebase.firestore.ListenerRegistration? = null

        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                userDocListener?.remove()
                userDocListener = null
                trySend(null)
            } else {
                userDocListener?.remove()
                userDocListener = firestore.collection("users").document(firebaseUser.uid)
                    .addSnapshotListener { doc, error ->
                        if (error != null) {
                            trySend(null)
                            return@addSnapshotListener
                        }
                        if (doc != null && doc.exists()) {
                            val dto = doc.toObject(UserDto::class.java)
                            if (dto != null) {
                                val user = dto.toDomain().copy(id = doc.id)
                                trySend(user)
                            } else {
                                trySend(null)
                            }
                        } else {
                            trySend(null)
                        }
                    }
            }
        }
        auth.addAuthStateListener(authListener)
        awaitClose {
            userDocListener?.remove()
            auth.removeAuthStateListener(authListener)
        }
    }
    
    override fun getUsers(): Flow<List<User>> = callbackFlow {
        val registration = firestore.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val users = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(UserDto::class.java)?.toDomain()?.copy(id = doc.id)
                    }
                    trySend(users)
                }
            }
        awaitClose { registration.remove() }
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> = try {
        auth.sendPasswordResetEmail(email).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> = try {
        val user = auth.currentUser ?: throw Exception("No authenticated user found.")
        user.updatePassword(newPassword).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
