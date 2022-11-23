package com.oldgoat5

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {

    try {
        println("Enter the file location of the members spreadsheet:")

        val filePath = readLine()!!

        println("filePath set to: $filePath")

        val file = File(filePath)

        println("Writing csv to output.csv...")

        val rows: List<Map<String, String>> = csvReader().readAllWithHeader(file)

        val header = listOf("name", "voter_identifier", "voter_key", "email", "vote_weight")
        val writeList: MutableList<List<String?>> = mutableListOf(header)

        rows.forEach { member ->
            if (member["STATUS"].equals("Active")
                && member["MEMBER_TYPE"].equals("Member")
                && member["EMAIL"] != null
            ) {
                val firstName = member["FIRST_NAME"]?.trim()
                val middleName = member["MIDDLE_NAME"]?.trim()
                val lastName = member["LAST_NAME"]?.trim()

                val fullName: String = if (middleName != null && middleName.isNotBlank()) {
                    "$firstName $middleName $lastName"
                } else {
                    "$firstName $lastName"
                }

                val email = member["EMAIL"]!!

                writeList.add(listOf(fullName, null, null, email, null))
            }
        }

        csvWriter().writeAll(writeList, "voters.csv")

        println("Success")
    } catch (e: Exception) {
        println("Error: $e")
        println("Current working directory: " + Paths.get("").toAbsolutePath().toString())
    } finally {
        println("Done")
    }
}