package services

import (
	"github.com/Dialosoft/management-service/src/adapters/repositories"
	"github.com/Dialosoft/management-service/src/domain/entities"
	"github.com/google/uuid"
)

type roleServiceImpl struct {
	roleRepo repositories.RoleRepository
}

// CreateRole implements RoleService.
func (impl *roleServiceImpl) CreateRole(roleType string, permission int, adminRole bool, modRole bool) error {
	role := entities.Role{
		RoleType:   roleType,
		Permission: permission,
		AdminRole:  adminRole,
		ModRole:    modRole,
	}

	if err := impl.roleRepo.Create(&role); err != nil {
		return err
	}

	return nil
}

// DeleteRole implements RoleService.
func (impl *roleServiceImpl) DeleteRole(id uuid.UUID) error {
	err := impl.roleRepo.Delete(id)
	if err != nil {
		return err
	}
	return nil
}

// GetAllRoles implements RoleService.
func (impl *roleServiceImpl) GetAllRoles() ([]*entities.Role, error) {
	roles, err := impl.roleRepo.FindAll()
	if err != nil {
		return nil, err
	}

	return roles, nil
}

// GetRoleByID implements RoleService.
func (impl *roleServiceImpl) GetRoleByID(id uuid.UUID) (*entities.Role, error) {
	role, err := impl.roleRepo.FindByID(id)
	if err != nil {
		return nil, err
	}

	return role, nil
}

// GetRoleByType implements RoleService.
func (impl *roleServiceImpl) GetRoleByType(roleType string) (*entities.Role, error) {
	role, err := impl.roleRepo.FindByType(roleType)
	if err != nil {
		return nil, err
	}

	return role, nil
}

// RestoreRole implements RoleService.
func (impl *roleServiceImpl) RestoreRole(id uuid.UUID) error {
	role, err := impl.roleRepo.FindByID(id)
	if err != nil {
		return err
	}

	err = impl.roleRepo.Restore(role.ID)
	if err != nil {
		return err
	}
	return nil
}

// UpdateRole implements RoleService.
func (impl *roleServiceImpl) UpdateRole(id uuid.UUID) error {
	err := impl.roleRepo.Update(id, nil)
	if err != nil {
		return err
	}

	// TO DO

	return nil
}

func (impl *roleServiceImpl) ChangeUserRole(roleID uuid.UUID, userID uuid.UUID) error {

	err := impl.roleRepo.UpdateUserRole(userID, roleID)
	if err != nil {
		return err
	}

	return nil
}

func NewRoleService(roleRepo repositories.RoleRepository) RoleService {
	return &roleServiceImpl{roleRepo: roleRepo}
}
