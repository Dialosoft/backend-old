package request

type Email struct {
	UserId   string `json:"userId"`
	NewEmail string `json:"newEmail"`
}
