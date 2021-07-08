package com.somacode.books.repository

import com.somacode.books.entity.oauth.OAuthClientDetails
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OAuthClientDetailsRepository: JpaRepository<OAuthClientDetails, String> {

}