package repositories

import (
	"github.com/Dialosoft/management-service/src/domain/entities"
	"github.com/google/uuid"
	"gorm.io/gorm"
)

type roleRepositoryImpl struct {
	db *gorm.DB
}

// FindAll implements RoleRepository.
func (repo *roleRepositoryImpl) FindAll() ([]*entities.Role, error) {
	var roles []*entities.Role
	result := repo.db.Find(&roles)
	if result.Error != nil {
		return nil, result.Error
	}

	return roles, nil
}

// FindByID implements RoleRepository.
func (repo *roleRepositoryImpl) FindByID(id uuid.UUID) (*entities.Role, error) {
	var role entities.Role
	result := repo.db.Find(&role, "id = ?", id.String())
	if result.Error != nil {
		return nil, result.Error
	}

	return &role, nil
}

// FindByName implements RoleRepository.
func (repo *roleRepositoryImpl) FindByType(roleType string) (*entities.Role, error) {
	var role entities.Role
	result := repo.db.Find(&role, "roleType = ?", roleType)
	if result.Error != nil {
		return nil, result.Error
	}

	return &role, nil
}

// Create implements RoleRepository.
func (repo *roleRepositoryImpl) Create(role *entities.Role) error {
	result := repo.db.Create(role)
	if result.Error != nil {
		return result.Error
	}

	return nil
}

// Delete implements RoleRepository.
func (repo *roleRepositoryImpl) Delete(id uuid.UUID) error {
	result := repo.db.Delete(&entities.Role{}, id)
	if result.Error != nil {
		return result.Error
	}

	return nil
}

// Restore implements RoleRepository.
func (repo *roleRepositoryImpl) Restore(id uuid.UUID) error {

	result := repo.db.Unscoped().Model(&entities.Forum{}).Where("id = ?", id).Update("deleted_at", nil)
	if result.Error != nil {
		return result.Error
	}

	return nil
}

// Update implements RoleRepository.
func (repo *roleRepositoryImpl) Update(id uuid.UUID, role *entities.Role) error {
	panic("unimplemented")
}

func NewRoleRepository(db *gorm.DB) RoleRepository {
	return &roleRepositoryImpl{db: db}
}
