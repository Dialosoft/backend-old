package entities

type Roles struct {
	Name      string `json:"name"`
	AdminRole bool   `json:"admin_role"`
	ModRole   bool   `json:"mod_role"`
}
