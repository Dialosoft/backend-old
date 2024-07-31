package services

type UserService interface {
	ChangeEmail(userID string, newMail string) error
	ChangeAvatar(userID string, avatar []byte) error
}
