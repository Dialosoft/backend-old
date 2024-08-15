package request

type Email struct {
	UserId   string `json:"userId"`
	NewEmail string `json:"newEmail"`
}

type Avatar struct {
	UserId string `json:"userID"`
	Avatar []byte `json:"avatar"`
}
