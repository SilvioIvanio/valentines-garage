package com.nust.valentinegarage.core.data.mapper

import com.nust.valentinegarage.core.data.dto.*
import com.nust.valentinegarage.core.model.*

fun UserDto.toDomain(): User = User(
    id = id,
    name = name,
    initials = initials,
    role = Role.valueOf(role.uppercase())
)

fun User.toDto(): UserDto = UserDto(
    id = id,
    name = name,
    initials = initials,
    role = role.name
)

fun VehicleDto.toDomain(): Vehicle = Vehicle(
    id = id,
    licensePlate = licensePlate,
    model = model
)

fun Vehicle.toDto(): VehicleDto = VehicleDto(
    id = id,
    licensePlate = licensePlate,
    model = model
)

fun CheckInDto.toDomain(): CheckIn = CheckIn(
    id = id,
    vehicleId = vehicleId,
    vehicleModel = vehicleModel,
    timestamp = timestamp,
    kilometersDriven = kilometersDriven,
    initialCondition = initialCondition,
    checkedInBy = checkedInBy,
    checkedInById = checkedInById,
    isCompleted = isCompleted
)

fun CheckIn.toDto(): CheckInDto = CheckInDto(
    id = id,
    vehicleId = vehicleId,
    vehicleModel = vehicleModel,
    timestamp = timestamp,
    kilometersDriven = kilometersDriven,
    initialCondition = initialCondition,
    checkedInBy = checkedInBy,
    checkedInById = checkedInById,
    isCompleted = isCompleted
)

fun TaskDto.toDomain(): Task = Task(
    id = id,
    name = name,
    description = description,
    status = TaskStatus.valueOf(status.uppercase()),
    priority = TaskPriority.valueOf(priority.uppercase()),
    mechanicId = mechanicId,
    mechanicName = mechanicName,
    mechanicInitials = mechanicInitials,
    completedAt = completedAt,
    notes = notes
)

fun Task.toDto(): TaskDto = TaskDto(
    id = id,
    name = name,
    description = description,
    status = status.name,
    priority = priority.name,
    mechanicId = mechanicId,
    mechanicName = mechanicName,
    mechanicInitials = mechanicInitials,
    completedAt = completedAt,
    notes = notes
)
