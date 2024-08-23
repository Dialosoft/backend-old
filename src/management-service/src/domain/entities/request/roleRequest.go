package request

type CreateRoleRequest struct {
	RoleType   string `json:"roleType"`
	Permission int    `json:"permission"`
	AdminRole  bool   `json:"adminRole"`
	ModRole    bool   `json:"modRole"`
}

type RoleUpdateRequest struct {
	RoleType   string `json:"roleType"`
	Permission int    `json:"permission"`
	AdminRole  bool   `json:"adminRole"`
	ModRole    bool   `json:"modRole"`
}
